using Application.IRepositories;
using Application.IServices;
using Application.Models.UpdateDtos;
using MongoDB.Bson;

namespace Infrastructure.Services;

public class RFIDTagsService
{
    private readonly IRFIDTagsRepository _rfidTagRepository;
    private readonly IUsagesRepository _usageRepository;
    private readonly IUsageService _usageService;
    private readonly IItemsRepository _itemsRepository;

    public RFIDTagsService(IRFIDTagsRepository rfidTagRepository, IUsagesRepository usageRepository, IUsageService usageService, IItemsRepository itemsRepository)
    {
        _rfidTagRepository = rfidTagRepository;
        _usageRepository = usageRepository;
        _usageService = usageService;
        _itemsRepository = itemsRepository;
    }

    public async Task<bool> UpdateTagAndIncrementUsageAsync(RFIDTagStatusUpdate statusUpdate, CancellationToken cancellationToken)
    {
        var objectId = ObjectId.Parse(statusUpdate.Id);
        var updateSuccessful = await _rfidTagRepository.UpdateStatus(statusUpdate, cancellationToken);
        var tag = await _rfidTagRepository.GetOneAsync(ObjectId.Parse(statusUpdate.Id), cancellationToken);
        var item = await _itemsRepository.GetOneAsync(x => x.Id == tag.ItemId, cancellationToken);
        
        if (statusUpdate.Status)
        {
            await _usageRepository.IncrementTotalCountAsync(tag.ItemId.ToString(), cancellationToken);
            var totalCount = await _usageService.CalculateTotalBrandUsageByUser(item.BrandId.ToString(), cancellationToken);
        }

        return true;
    }
}