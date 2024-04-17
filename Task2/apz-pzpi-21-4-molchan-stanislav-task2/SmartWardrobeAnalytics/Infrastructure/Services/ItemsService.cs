using Application.IRepositories;
using Application.IServices;
using Application.Models.CreateDtos;
using Application.Models.Dtos;
using Application.Models.UpdateDtos;
using Application.Paging;
using AutoMapper;
using Domain.Entities;
using MongoDB.Bson;

namespace Infrastructure.Services;

public class ItemsService : IItemsService
{
    private readonly IItemsRepository _itemsRepository;
    private readonly IMapper _mapper;

    public ItemsService(IItemsRepository itemsRepository, IMapper mapper)
    {
        _itemsRepository = itemsRepository;
        _mapper = mapper;
    }

    public async Task<ItemDto> GetItemByIdAsync(string id, CancellationToken cancellationToken)
    {
        var item = await _itemsRepository.GetOneAsync(ObjectId.Parse(id), cancellationToken);
        var dto = _mapper.Map<ItemDto>(item);
        dto.Categories = item.Category;
        return dto;
    }

    public async Task<ItemDto> CreateItemAsync(ItemCreateDto dto, CancellationToken cancellationToken)
    {
        var item = _mapper.Map<Item>(dto);
        item.CreatedDateUtc = DateTime.UtcNow;

        await _itemsRepository.AddAsync(item, cancellationToken);

        return _mapper.Map<ItemDto>(item);
    }

    public async Task<ItemDto> UpdateItemAsync(ItemUpdateDto dto, CancellationToken cancellationToken)
    {
        var existingItem = await _itemsRepository.GetOneAsync(ObjectId.Parse(dto.Id), cancellationToken);

        var updatedItem = this._mapper.Map(dto, existingItem);
        
        var result = await _itemsRepository.UpdateAsync(updatedItem, cancellationToken);

        var itemDto = _mapper.Map<ItemDto>(result);
        itemDto.Categories = dto.Categories;
        
        return itemDto;
    }

    public async Task<bool> DeleteItemAsync(string id, CancellationToken cancellationToken)
    {
        var existingItem = await _itemsRepository.GetOneAsync(ObjectId.Parse(id), cancellationToken);

        if (existingItem == null)
        {
            return false;
        }

        await _itemsRepository.DeleteAsync(existingItem, cancellationToken);
        return true;
    }

    public async Task<PagedList<ItemDto>> GetItemsWithPaginationAsync(int pageNumber, int pageSize, CancellationToken cancellationToken)
    {
        var items = await _itemsRepository.GetPageAsync(pageNumber, pageSize, cancellationToken);
        var dtos = _mapper.Map<List<ItemDto>>(items);
        var totalCount = await _itemsRepository.GetTotalCountAsync();

        return new PagedList<ItemDto>(dtos, pageNumber, pageSize, totalCount);
    }
}