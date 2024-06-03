using Application.IRepositories.Base;
using Domain.Entities;
using MongoDB.Bson;

namespace Application.IRepositories;

public interface IUsagesRepository : IBaseRepository<Usages>
{
    Task<Usages> UpdateAsync(Usages usages, CancellationToken cancellationToken);

    Task<Usages> IncrementTotalCountAsync(string id, CancellationToken cancellationToken);

    Task<int> GetTotalUsageCountForItems(List<ObjectId> itemIds, CancellationToken cancellationToken);
}