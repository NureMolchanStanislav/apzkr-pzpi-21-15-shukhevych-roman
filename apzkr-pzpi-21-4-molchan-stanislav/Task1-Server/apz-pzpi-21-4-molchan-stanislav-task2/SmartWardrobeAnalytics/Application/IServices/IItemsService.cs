using Application.Models.CreateDtos;
using Application.Models.Dtos;
using Application.Models.UpdateDtos;
using Application.Paging;

namespace Application.IServices;

public interface IItemsService
{
    Task<ItemDto> GetItemByIdAsync(string id, CancellationToken cancellationToken);

    Task<List<ItemDto>> GetItemByCollectionIdAsync(string id,
        CancellationToken cancellationToken);
    Task<ItemDto> CreateItemAsync(ItemCreateDto dto, CancellationToken cancellationToken);

    Task<ItemDto> UpdateItemAsync(ItemUpdateDto dto, CancellationToken cancellationToken);

    Task<bool> DeleteItemAsync(string id, CancellationToken cancellationToken);

    Task<PagedList<ItemDto>> GetItemsWithPaginationAsync(int pageNumber, int pageSize,
        CancellationToken cancellationToken);
}