using Application.Models.Identity;

namespace Application.Models.UpdateDtos;

public class UpdateUserModel
{
    public TokensModel Tokens { get; set; }

    public UserUpdateDto User { get; set; }
}