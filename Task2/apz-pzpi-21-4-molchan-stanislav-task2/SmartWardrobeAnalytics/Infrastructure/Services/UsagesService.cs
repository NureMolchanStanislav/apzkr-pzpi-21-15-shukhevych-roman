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

public class UsagesService : IUsageService
{
    private readonly IUsagesRepository _usageRepository;
    private readonly IBrandsRepository _brandsRepository;
    private readonly IItemsRepository _itemsRepository;
    private readonly IMapper _mapper;

    public UsagesService(IUsagesRepository usageRepository, IMapper mapper, IBrandsRepository brandsRepository, IItemsRepository itemsRepository)
    {
        _usageRepository = usageRepository;
        _mapper = mapper;
        _brandsRepository = brandsRepository;
        _itemsRepository = itemsRepository;
    }

    public async Task<UsageDto> GetByIdAsync(string id, CancellationToken cancellationToken)
    {
        var usage = await _usageRepository.GetOneAsync(ObjectId.Parse(id), cancellationToken);
        return _mapper.Map<UsageDto>(usage);
    }

    public async Task<UsageDto> CreateAsync(UsagesCreateDto dto, CancellationToken cancellationToken)
    {
        var usage = _mapper.Map<Usages>(dto);
        usage.CreatedDateUtc = DateTime.UtcNow;
        usage.CreatedById = GlobalUser.Id;

        await _usageRepository.AddAsync(usage, cancellationToken);

        return _mapper.Map<UsageDto>(usage);
    }

    public async Task<UsageDto> UpdateAsync(UsagesUpdateDto dto, CancellationToken cancellationToken)
    {
        var existingUsage = await _usageRepository.GetOneAsync(ObjectId.Parse(dto.Id), cancellationToken);
        if (existingUsage == null)
        {
            return null;
        }

        _mapper.Map(dto, existingUsage);
        await _usageRepository.UpdateAsync(existingUsage, cancellationToken);

        return _mapper.Map<UsageDto>(existingUsage);
    }

    public async Task<bool> DeleteAsync(string id, CancellationToken cancellationToken)
    {
        var existingUsage = await _usageRepository.GetOneAsync(ObjectId.Parse(id), cancellationToken);
        if (existingUsage == null)
        {
            return false;
        }

        await _usageRepository.DeleteAsync(existingUsage, cancellationToken);
        return true;
    }

    public async Task<PagedList<UsageDto>> GetWithPaginationAsync(int pageNumber, int pageSize, CancellationToken cancellationToken)
    {
        var usages = await _usageRepository.GetPageAsync(pageNumber, pageSize, cancellationToken);
        var dtos = _mapper.Map<List<UsageDto>>(usages);
        var totalCount = await _usageRepository.GetTotalCountAsync();

        return new PagedList<UsageDto>(dtos, pageNumber, pageSize, totalCount);
    }
    
    public async Task<int> CalculateTotalBrandUsageByUser(string brandId, CancellationToken cancellationToken)
    {
        List<Item> items = await _itemsRepository.GetItemsByBrandAndCreatorId(ObjectId.Parse(brandId), GlobalUser.Id, cancellationToken);
        if (!items.Any()) return 0;

        List<ObjectId> itemIds = items.Select(item => item.Id).ToList();
        int totalUsages = await _usageRepository.GetTotalUsageCountForItems(itemIds, cancellationToken);

        return totalUsages;
    }
}