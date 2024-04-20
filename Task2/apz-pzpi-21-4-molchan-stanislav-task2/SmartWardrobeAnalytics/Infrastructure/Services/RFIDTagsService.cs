using Application.GlobalInstance;
using Application.IRepositories;
using Application.IServices;
using Application.Models.CreateDtos;
using Application.Models.Dtos;
using Application.Models.StatisticsDtos;
using Application.Models.UpdateDtos;
using Domain.Entities;
using MongoDB.Bson;

namespace Infrastructure.Services;

public class RFIDTagsService : IRFIDTagsService
{
    private readonly IRFIDTagsRepository _rfidTagRepository;
    private readonly IUsagesRepository _usageRepository;
    private readonly IUsageService _usageService;
    private readonly IItemsRepository _itemsRepository;
    private readonly IBrandBonusesRepository _brandBonusesRepository;
    private readonly IOffersRepository _offersRepository;
    private readonly IUsageHistoryRepository _usageHistoryRepository;

    public RFIDTagsService(IRFIDTagsRepository rfidTagRepository, IUsagesRepository usageRepository, IUsageService usageService, IItemsRepository itemsRepository, IBrandBonusesRepository brandBonusesRepository, IOffersRepository offersRepository, IUsageHistoryRepository usageHistoryRepository)
    {
        _rfidTagRepository = rfidTagRepository;
        _usageRepository = usageRepository;
        _usageService = usageService;
        _itemsRepository = itemsRepository;
        _brandBonusesRepository = brandBonusesRepository;
        _offersRepository = offersRepository;
        _usageHistoryRepository = usageHistoryRepository;
    }

    public async Task<bool> UpdateTagAndIncrementUsageAsync(RFIDTagStatusUpdate statusUpdate, CancellationToken cancellationToken)
    {
        var objectId = ObjectId.Parse(statusUpdate.Id);
        var updateSuccessful = await _rfidTagRepository.UpdateStatus(statusUpdate, cancellationToken);
        var tag = await _rfidTagRepository.GetOneAsync(ObjectId.Parse(statusUpdate.Id), cancellationToken);
        var item = await _itemsRepository.GetOneAsync(x => x.Id == tag.ItemId, cancellationToken);
        
        if (updateSuccessful && statusUpdate.Status)
        {
            var usageHistory = new UsageHistory
            {
                ItemId = tag.ItemId,
                Event = "Used",
                CreatedDateUtc = DateTime.UtcNow
            };
            await _usageHistoryRepository.AddAsync(usageHistory, cancellationToken);
            
            await _usageRepository.IncrementTotalCountAsync(tag.ItemId.ToString(), cancellationToken);
            var totalCount = await _usageService.CalculateTotalBrandUsageByUser(item.BrandId.ToString(), cancellationToken);
            
            var brandBonus = await _brandBonusesRepository.GetOneAsync(x => x.BrandId == item.BrandId, cancellationToken);
            if (brandBonus != null && totalCount > brandBonus.ConditionalNumberOfUsages)
            {
                var currentOffer = await _offersRepository.GetOneAsync(x => x.BrandId == item.BrandId && x.UserId == GlobalUser.Id, cancellationToken);
                if (currentOffer != null && currentOffer.Discount < brandBonus.MaxDiscount)
                {
                    currentOffer.Discount += brandBonus.ConditionalDiscount;
                    await _offersRepository.UpdateAsync(currentOffer, cancellationToken);
                }
                
                brandBonus.ConditionalNumberOfUsages *= brandBonus.ConditionalComplexity;
                await _brandBonusesRepository.UpdateAsync(brandBonus, cancellationToken);
            }
        }

        return true;
    }
    
    public async Task<RFIDTagDto> CreateRFIDTagAsync(RFIDTagCreateDto createDto, CancellationToken cancellationToken)
    {
        RFIDTag newTag = new RFIDTag
        {
            ItemId = ObjectId.Parse(createDto.ItemId),
            CreatedDateUtc= DateTime.UtcNow,
            CreatedById = GlobalUser.Id
        };

        await _rfidTagRepository.AddAsync(newTag, cancellationToken);
        
        return new RFIDTagDto
        {
            Id = newTag.Id.ToString(),
        };
    }
}