using Application.Models.CreateDtos;
using Application.Models.Dtos;
using Application.Models.UpdateDtos;
using Application.Paging;
using Domain.Entities;

namespace Application.IServices;

public interface ICollectionsService
{
    Task<Collection> GetCollectionByIdAsync(string id, CancellationToken cancellationToken);
    Task<Collection> CreateCollectionAsync(CollectionCreateDto collection, CancellationToken cancellationToken);
    Task<CollectionDto> UpdateCollectionAsync(CollectionUpdateDto collection, CancellationToken cancellationToken);
    Task<bool> DeleteCollectionAsync(string id, CancellationToken cancellationToken);

    Task<PagedList<CollectionDto>> GetCollectionsWithPaginationAsync(int pageNumber, int pageSize,
        CancellationToken cancellationToken);
}