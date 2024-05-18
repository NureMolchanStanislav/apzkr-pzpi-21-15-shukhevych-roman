using Application.Models.CreateDtos;
using Application.Models.Dtos;
using Application.Models.Identity;
using Application.Models.UpdateDtos;
using Application.Paging;

namespace Application.IServices;

public interface IUserService
{
    Task<TokensModel> AddUserAsync(UserCreateDto dto, CancellationToken cancellationToken);

    Task<UserDto> GetUserAsync(string id, CancellationToken cancellationToken);

    Task<PagedList<UserDto>> GetUsersPageAsync(int pageNumber, int pageSize, CancellationToken cancellationToken);

    Task<UpdateUserModel> UpdateAsync(UserUpdateDto userDto, CancellationToken cancellationToken);

    Task<TokensModel> LoginAsync(LoginUserDto login, CancellationToken cancellationToken);

    Task<UserDto> AddToRoleAsync(string userId, string roleName, CancellationToken cancellationToken);

    Task<UserDto> RemoveFromRoleAsync(string userId, string roleName, CancellationToken cancellationToken);

    Task<TokensModel> RefreshAccessTokenAsync(TokensModel tokensModel, CancellationToken cancellationToken);

    Task<UserDto> GetCurrentUserAsync(CancellationToken cancellationToken);

    Task<bool> BanUser(string userId, CancellationToken cancellationToken);

    Task<bool> UnBanUser(string userId, CancellationToken cancellationToken);
}