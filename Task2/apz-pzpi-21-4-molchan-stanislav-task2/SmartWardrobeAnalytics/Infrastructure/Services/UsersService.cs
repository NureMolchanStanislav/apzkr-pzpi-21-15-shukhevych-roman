using System.Security.Claims;
using System.Text.RegularExpressions;
using Application.GlobalInstance;
using Application.IRepositories;
using Application.IServices;
using Application.IServices.Identity;
using Application.Models.CreateDtos;
using Application.Models.Dtos;
using Application.Models.Identity;
using Application.Models.UpdateDtos;
using Application.Paging;
using AutoMapper;
using Domain.Entities;
using Microsoft.IdentityModel.Tokens;
using MongoDB.Bson;

namespace Infrastructure.Services;

public class UsersService : IUserService 
{
    private readonly IUsersRepository _usersRepository;

    private readonly IRolesRepository _rolesRepository;

    private readonly ITokenService _tokensService;

    private IRefreshTokensRepository _refreshTokensRepository;

    private readonly IMapper _mapper;

    private readonly IPasswordHasher _passwordHasher;

    public UsersService(IUsersRepository usersRepository, IRolesRepository rolesRepository, ITokenService tokensService, 
    IRefreshTokensRepository refreshTokensRepository, IPasswordHasher passwordHasher, IMapper mapper)
    {
        _usersRepository = usersRepository;
        _rolesRepository = rolesRepository;
        _tokensService = tokensService;
        _refreshTokensRepository = refreshTokensRepository;
        _passwordHasher = passwordHasher;
        _mapper = mapper;
    }
    
    public async Task<UserDto> GetCurrentUserAsync(CancellationToken cancellationToken)
    {
        var entity = await _usersRepository.GetOneAsync(x=>x.Id==GlobalUser.Id, cancellationToken);
        if (entity == null)
        {
            throw new Exception();
        }
        
        return _mapper.Map<UserDto>(entity);
    }

    public async Task<PagedList<UserDto>> GetUsersPageAsync(int pageNumber, int pageSize, CancellationToken cancellationToken)
    {
        var entities = await _usersRepository.GetPageAsync(pageNumber, pageSize, cancellationToken);
        var dtos = _mapper.Map<List<UserDto>>(entities);
        var count = await _usersRepository.GetTotalCountAsync();
        return new PagedList<UserDto>(dtos, pageNumber, pageSize, count);
    }

    public async Task<UserDto> GetUserAsync(string id, CancellationToken cancellationToken)
    {
        if (!ObjectId.TryParse(id, out var objectId))
        {
            throw new InvalidDataException("Provided id is invalid.");
        }

        var entity = await _usersRepository.GetOneAsync(objectId, cancellationToken);
        if (entity == null)
        {
            throw new Exception(id);
        }

        return _mapper.Map<UserDto>(entity);
    }

    public async Task<TokensModel> AddUserAsync(UserCreateDto dto, CancellationToken cancellationToken)
    {
        var userDto = new UserDto 
        { 
            Email = dto.Email, 
            Phone = dto.Phone
        };
        
        await ValidateUserAsync(userDto, new User(), cancellationToken);

        var role = await _rolesRepository.GetOneAsync(r => r.Name == "User", cancellationToken);
        var user = new User
        {
            FirstName = dto.FirstName,
            LastName = dto.LastName,
            Email = dto.Email,
            Roles = new List<Role>{role},
            Phone = dto.Phone,
            PasswordHash = _passwordHasher.Hash(dto.Password),
            CreatedDateUtc = DateTime.UtcNow,
            CreatedById = ObjectId.Empty
        };

        await _usersRepository.AddAsync(user, cancellationToken);
        var refreshToken = await AddRefreshToken(user.Id, cancellationToken);
        var tokens = GetUserTokens(user, refreshToken);

        return tokens;
    }
    

    public async Task<TokensModel> LoginAsync(LoginUserDto login, CancellationToken cancellationToken)
    {

        var user = await _usersRepository.GetOneAsync(u => u.Email == login.Email, cancellationToken);

        if (user == null)
        {
            throw new Exception("User with this email was not found");
        }

        if (!_passwordHasher.Check(login.Password, user.PasswordHash))
        {
            throw new InvalidDataException("Invalid password!");
        }

        var refreshToken = await AddRefreshToken(user.Id, cancellationToken);

        var tokens = GetUserTokens(user, refreshToken);
        GlobalUser.Id = user.Id;

        return tokens;
    }

    public async Task<UpdateUserModel> UpdateAsync(UserUpdateDto userDto, CancellationToken cancellationToken)
    {

        var user = await _usersRepository.GetOneAsync(x => x.Id == ObjectId.Parse(userDto.Id), cancellationToken);
        if (user == null)
        {
            throw new Exception("User with this id was not found");
        }
        
        var userDtoForValidate = new UserDto 
        { 
            Email = userDto.Email, 
            Phone = userDto.Phone
        };
        
        await ValidateUserAsync(userDtoForValidate, new User(), cancellationToken);

        _mapper.Map(userDto, user);
        if (!string.IsNullOrEmpty(userDto.Password))
        {
            user.PasswordHash = _passwordHasher.Hash(userDto.Password);
        }
        
        var updatedUser = await _usersRepository.UpdateUserAsync(user, cancellationToken);

        var refreshToken = await AddRefreshToken(user.Id, cancellationToken);

        var tokens = GetUserTokens(user, refreshToken);

        var updatedUserDto = _mapper.Map<UserUpdateDto>(updatedUser);

        return new UpdateUserModel() 
        { 
            Tokens = tokens, 
            User = updatedUserDto
        };
    }

    public async Task<TokensModel> RefreshAccessTokenAsync(TokensModel tokensModel, CancellationToken cancellationToken)
    {
        var principal = _tokensService.GetPrincipalFromExpiredToken(tokensModel.AccessToken);
        var userId = ObjectId.Parse(principal.Claims.FirstOrDefault(c => c.Type == ClaimTypes.NameIdentifier)?.Value);

        var refreshTokenModel = await this._refreshTokensRepository
            .GetOneAsync(r => 
                r.Token == tokensModel.RefreshToken 
                && r.CreatedById == userId
                && r.IsDeleted == false, cancellationToken);
        if (refreshTokenModel == null || refreshTokenModel.ExpiryDateUTC < DateTime.UtcNow)
        {
            throw new SecurityTokenExpiredException("Refresh Token expired.");
        }

        var refreshToken = refreshTokenModel.Token;

        // Update Refresh token if it expires in less than 7 days to keep user constantly logged in if he uses the app
        if (refreshTokenModel.ExpiryDateUTC.AddDays(-7) < DateTime.UtcNow)
        {
            await _refreshTokensRepository.DeleteAsync(refreshTokenModel, cancellationToken);
            
            var newRefreshToken = await AddRefreshToken(userId, cancellationToken);
            refreshToken = newRefreshToken.Token;
        }

        var tokens = new TokensModel
        {
            AccessToken = _tokensService.GenerateAccessToken(principal.Claims),
            RefreshToken = refreshToken
        };
        

        return tokens;
    }
    
    public async Task<UserDto> AddToRoleAsync(string userId, string roleName, CancellationToken cancellationToken)
    {
        var role = await _rolesRepository.GetOneAsync(r => r.Name == roleName, cancellationToken);

        if (role==null)
        {
            throw new ArgumentNullException($"{roleName} is not found");
        }

        var userObjectId = ObjectId.Parse(userId);
        var user = await _usersRepository.GetOneAsync(userObjectId, cancellationToken);

        if (user == null)
        {
            throw new ArgumentNullException("User");
        }
        
        user.Roles.Add(role);
        var updateUser = await this._usersRepository.UpdateUserAsync(user, cancellationToken);
        var userDto = this._mapper.Map<UserDto>(updateUser);

        return userDto;
    }
    
    public async Task<UserDto> RemoveFromRoleAsync(string userId, string roleName, CancellationToken cancellationToken)
    {
        var role = await _rolesRepository.GetOneAsync(r => r.Name == roleName, cancellationToken);

        if (role==null)
        {
            throw new ArgumentNullException($"{roleName} is not found");
        }

        var userObjectId = ObjectId.Parse(userId);
        var user = await _usersRepository.GetOneAsync(userObjectId, cancellationToken);

        if (user == null)
        {
            throw new ArgumentNullException("User");
        }

        var deletedRole = user.Roles.Find(r => r.Name == role.Name);
        user.Roles.Remove(deletedRole);
        
        var updateUser = await _usersRepository.UpdateUserAsync(user, cancellationToken);
        var userDto = _mapper.Map<UserDto>(updateUser);

        return userDto;
    }

    private TokensModel GetUserTokens(User user, RefreshToken refreshToken)
    {
        var claims = GetClaims(user);
        var accessToken = _tokensService.GenerateAccessToken(claims);


        return new TokensModel
        {
            AccessToken = accessToken,
            RefreshToken = refreshToken.Token,
        };
    }

    private IEnumerable<Claim> GetClaims(User user)
    {
        var claims = new List<Claim>()
        {
            new (ClaimTypes.NameIdentifier, user.Id.ToString()),
            new (ClaimTypes.Email, user.Email ?? string.Empty),
            new (ClaimTypes.MobilePhone, user.Phone ?? string.Empty),
        };
        
        foreach (var role in user.Roles)
        {
            claims.Add(new (ClaimTypes.Role, role.Name));
        }

        return claims;
    }
    
    private async Task<RefreshToken> AddRefreshToken(ObjectId userId, CancellationToken cancellationToken)
    {
        var refreshToken = new RefreshToken
        {
            Token = _tokensService.GenerateRefreshToken(),
            ExpiryDateUTC = DateTime.UtcNow.AddDays(10),
            CreatedDateUtc = DateTime.UtcNow
        };

        await this._refreshTokensRepository.AddAsync(refreshToken, cancellationToken);

        return refreshToken;
    }
    
    private void ValidateEmail(string email)
    {
        string regex = @"^[^@\s]+@[^@\s]+\.[^@\s]+$";

        if (!Regex.IsMatch(email, regex))
        {
            /*throw new InvalidEmailException(email);*/
        }
    }

    private void ValidatePhone(string phone)
    {
        string regex = @"^\+[0-9]{1,15}$";

        if (!Regex.IsMatch(phone, regex))
        {
            /*throw new InvalidPhoneNumberException(phone);*/
        }
    }
    
    private async Task ValidateUserAsync(UserDto userDto, User user, CancellationToken cancellationToken)
    {
        if (!string.IsNullOrEmpty(userDto.Email)) 
        {
            ValidateEmail(userDto.Email);
            if (userDto.Email != user.Email 
                && await this._usersRepository.ExistsAsync(x => x.Email == userDto.Email, cancellationToken))
            {
                /*throw new EntityAlreadyExistsException("User", "Email", userDto.Email);*/
            }
        }

        if (!string.IsNullOrEmpty(userDto.Phone)) 
        {
            ValidatePhone(userDto.Phone);
            if (userDto.Phone != user.Phone 
                && await this._usersRepository.ExistsAsync(x => x.Phone == userDto.Phone, cancellationToken))
            {
                /*throw new EntityAlreadyExistsException("User", "Phone", userDto.Phone);*/
            }
        }
    }
}