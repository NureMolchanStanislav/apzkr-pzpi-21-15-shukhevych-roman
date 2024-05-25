using Application.GlobalInstance;
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

public class CollectionsService : ICollectionsService
{
    private readonly ICollectionsRepository _collectionsRepository;
    private readonly IMapper _mapper;

    public CollectionsService(ICollectionsRepository collectionsRepository, IMapper mapper)
    {
        _collectionsRepository = collectionsRepository;
        _mapper = mapper;
    }

    public async Task<CollectionDto> GetCollectionByIdAsync(string id, CancellationToken cancellationToken)
    {
        var result = await _collectionsRepository.GetOneAsync(ObjectId.Parse(id), cancellationToken);
        return _mapper.Map<CollectionDto>(result);
    }
    
    public async Task<List<CollectionDto>> GetAllAsync(CancellationToken cancellationToken)
    {
        var collections =
            await _collectionsRepository.GetAllAsync(cancellationToken);
        var dtos = _mapper.Map<List<CollectionDto>>(collections);
        return dtos;
    }

    public async Task<Collection> CreateCollectionAsync(CollectionCreateDto dto, CancellationToken cancellationToken)
    {
        var collection = new Collection()
        {
            Name = dto.Name,
            Description = dto.Description,
            CreatedDateUtc = DateTime.UtcNow,
            CreatedById = GlobalUser.Id,
        };
        
        return await _collectionsRepository.AddAsync(collection, cancellationToken);
    }

    public async Task<CollectionDto> UpdateCollectionAsync(CollectionUpdateDto collection, CancellationToken cancellationToken)
    {
        var existingCollection = await _collectionsRepository.GetOneAsync(ObjectId.Parse(collection.Id), cancellationToken);

        var updatedCollection = this._mapper.Map(collection, existingCollection);

        var result = await _collectionsRepository.UpdateAsync(updatedCollection, cancellationToken);

        return _mapper.Map<CollectionDto>(result);
    }

    public async Task<bool> DeleteCollectionAsync(string id, CancellationToken cancellationToken)
    {
        var collection = await _collectionsRepository.GetOneAsync(ObjectId.Parse(id), cancellationToken);
        if (collection == null)
        {
            return false;
        }

        await _collectionsRepository.DeleteAsync(collection, cancellationToken);
        return true;
    }

    public async Task<PagedList<CollectionDto>> GetCollectionsWithPaginationAsync(int pageNumber, int pageSize, CancellationToken cancellationToken)
    {
        var collections = await _collectionsRepository.GetPageAsync(pageNumber, pageSize, x=>x.CreatedById == GlobalUser.Id, cancellationToken);
        var dtos = _mapper.Map<List<CollectionDto>>(collections);
        var totalCount = await _collectionsRepository.GetTotalCountAsync();

        return new PagedList<CollectionDto>(dtos, pageNumber, pageSize, totalCount);
    }
}