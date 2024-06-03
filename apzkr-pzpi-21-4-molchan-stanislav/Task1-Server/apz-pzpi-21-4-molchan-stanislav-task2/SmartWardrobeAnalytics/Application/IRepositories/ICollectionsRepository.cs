using Application.IRepositories.Base;
using Domain.Entities;

namespace Application.IRepositories;

public interface ICollectionsRepository : IBaseRepository<Collection>
{
    Task<Collection> UpdateAsync(Collection collection, CancellationToken cancellationToken);
}