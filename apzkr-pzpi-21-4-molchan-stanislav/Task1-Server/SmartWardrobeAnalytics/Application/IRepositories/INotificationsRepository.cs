using Application.IRepositories.Base;
using Domain.Entities;

namespace Application.IRepositories;

public interface INotificationsRepository : IBaseRepository<Notification>
{
    Task<Notification> UpdateAsync(Notification item, CancellationToken cancellationToken);
}