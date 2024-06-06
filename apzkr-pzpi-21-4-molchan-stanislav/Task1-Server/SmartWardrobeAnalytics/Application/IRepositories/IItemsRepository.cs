using Application.IRepositories.Base;
using Domain.Entities;
using MongoDB.Bson;

namespace Application.IRepositories;

public interface IItemsRepository : IBaseRepository<Item>
{
    Task<Item> UpdateAsync(Item item, CancellationToken cancellationToken);

    Task<List<Item>> GetItemsByBrandAndCreatorId(ObjectId brandId, ObjectId creatorId,
        CancellationToken cancellationToken);

    Task<List<Item>> GetAllAsync(CancellationToken cancellationToken = default);
}