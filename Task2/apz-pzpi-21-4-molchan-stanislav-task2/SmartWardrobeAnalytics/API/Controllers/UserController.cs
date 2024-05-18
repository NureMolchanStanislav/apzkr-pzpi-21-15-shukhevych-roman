using System.Security.Claims;
using API.Controllers.Base;
using Application.IServices;
using Application.Models.CreateDtos;
using Application.Models.Dtos;
using Application.Models.Identity;
using Application.Models.UpdateDtos;
using Application.Paging;
using Microsoft.AspNetCore.Mvc;

namespace API.Controllers;

[Route("api/users")]
public class UserController : BaseController 
{
    private readonly IUserService _userService;

    public UserController(IUserService userService)
    {
        _userService = userService;
    }

    [HttpPost("register")]
    public async Task<ActionResult> RegisterAsync([FromBody] UserCreateDto register, CancellationToken cancellationToken)
    {
        var result = await _userService.AddUserAsync(register, cancellationToken);
        return Ok(result);
    }

    [HttpPost("login")]
    public async Task<ActionResult<TokensModel>> LoginAsync([FromBody] LoginUserDto login, CancellationToken cancellationToken)
    {
        var tokens = await _userService.LoginAsync(login, cancellationToken);
        return Ok(tokens);
    }
    
    [HttpPut]
    public async Task<ActionResult<UpdateUserModel>> UpdateAsync([FromBody] UserUpdateDto userDto, CancellationToken cancellationToken)
    {
        var updatedUserModel = await _userService.UpdateAsync(userDto, cancellationToken);
        return Ok(updatedUserModel);
    }
    
    [HttpGet("{id}")]
    public async Task<ActionResult<UserDto>> GetUserAsync(string id, CancellationToken cancellationToken)
    {
        var user = await _userService.GetUserAsync(id, cancellationToken);
        return Ok(user);
    }
    
    [HttpGet]
    public async Task<ActionResult<PagedList<UserDto>>> GetUsersPageAsync([FromQuery] int pageNumber, [FromQuery] int pageSize, CancellationToken cancellationToken)
    {
        var users = await _userService.GetUsersPageAsync(pageNumber, pageSize, cancellationToken);
        return Ok(users);
    }
    
    [HttpGet("get")]
    public async Task<ActionResult<UserDto>> GetCurrentUser(CancellationToken cancellationToken)
    {
        var user = await _userService.GetCurrentUserAsync(cancellationToken);

        return user;
    }
    
    [HttpDelete("ban/{id}")]
    public async Task<bool> BanUser(string id, CancellationToken cancellationToken)
    {
        return await _userService.BanUser(id, cancellationToken);
    }
    
    [HttpPost("unban/{id}")]
    public async Task<bool> UnBanUser(string id, CancellationToken cancellationToken)
    {
        return await _userService.UnBanUser(id, cancellationToken);
    }

    [HttpPost("{userId}/roles/{roleName}")]
    public async Task<ActionResult<PagedList<UserDto>>> AddToRoleAsync(string userId, string roleName, CancellationToken cancellationToken)
    {
        var users = await _userService.AddToRoleAsync(userId, roleName, cancellationToken);
        return Ok(users);
    }
    
    [HttpPost("token/refresh")]
    public async Task<ActionResult<TokensModel>> RefreshAccessTokenAsync([FromBody] TokensModel tokensModel, CancellationToken cancellationToken)
    {
        var refreshedTokens = await _userService.RefreshAccessTokenAsync(tokensModel, cancellationToken);
        return Ok(refreshedTokens);
    }
    
    [HttpDelete("{userId}/roles/{roleName}")]
    public async Task<ActionResult<PagedList<UserDto>>> RemoveFromeRoleAsync(string userId, string roleName, CancellationToken cancellationToken)
    {
        var users = await _userService.RemoveFromRoleAsync(userId, roleName, cancellationToken);
        return Ok(users);
    }
}