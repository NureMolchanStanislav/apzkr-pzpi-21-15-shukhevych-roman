using Application.IRepositories.Base;
using Domain.Entities;

namespace Application.IRepositories;

public interface IItemsRepository : IBaseRepository<Item>
{
    Task<Item> UpdateAsync(Item item, CancellationToken cancellationToken);
}