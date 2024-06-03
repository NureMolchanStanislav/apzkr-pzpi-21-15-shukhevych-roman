using Application.IRepositories.Base;
using Domain.Entities;

namespace Application.IRepositories;

public interface IUsersRepository : IBaseRepository<User>
{
    Task<User> UpdateUserAsync(User user, CancellationToken cancellationToken);

    Task<bool> UnBan(string id, CancellationToken cancellationToken);
}