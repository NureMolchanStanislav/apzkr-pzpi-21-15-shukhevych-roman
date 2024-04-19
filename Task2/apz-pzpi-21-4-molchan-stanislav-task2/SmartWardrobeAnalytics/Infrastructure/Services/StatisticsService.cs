using Application.IRepositories;
using Application.IServices.Statistics;
using Application.Models.StatisticsDtos;
using MongoDB.Bson;

namespace Infrastructure.Services;

public class StatisticsService : IStatisticsService
{
    private readonly IUsageHistoryRepository _usageHistoryRepository;

    private readonly IItemsRepository _itemsRepository;

    public StatisticsService(IUsageHistoryRepository usageHistoryRepository, IItemsRepository itemsRepository)
    {
        _usageHistoryRepository = usageHistoryRepository;
        _itemsRepository = itemsRepository;
    }

    public async Task<List<SeasonalItemUsageStatisticsDto>> GetSeasonalItemUsageStatisticsAsync(ObjectId brandId, CancellationToken cancellationToken)
    {
        var items = await _itemsRepository.GetPageAsync(1, 100, x => x.BrandId == brandId, cancellationToken);
        
        var usageHistory = await _usageHistoryRepository.GetPageAsync(1, 100, cancellationToken);

        var seasonalUsages = new Dictionary<string, Dictionary<string, int>>();
        foreach (var usage in usageHistory)
        {
            var item = items.FirstOrDefault(x => x.Id == usage.ItemId);
            if (item == null) continue;

            var season = GetSeason(usage.CreatedDateUtc);
            if (!seasonalUsages.ContainsKey(season))
            {
                seasonalUsages[season] = new Dictionary<string, int>();
            }

            var itemName = item.Name;
            if (!seasonalUsages[season].ContainsKey(itemName))
            {
                seasonalUsages[season][itemName] = 0;
            }

            seasonalUsages[season][itemName]++;
        }

        var seasonalStatisticsList = new List<SeasonalItemUsageStatisticsDto>();
        foreach (var season in seasonalUsages)
        {
            var seasonalStatistics = new SeasonalItemUsageStatisticsDto
            {
                Season = season.Key,
                TotalUsages = season.Value.Values.Sum(),
                ItemUsages = season.Value
            };
            seasonalStatisticsList.Add(seasonalStatistics);
        }

        return seasonalStatisticsList;
    }
    
    public async Task<List<CombinationStatisticsDto>> GetItemCombinationsStatisticsAsync(CancellationToken cancellationToken)
    {
        var usageHistory = await _usageHistoryRepository.GetAllAsync(cancellationToken);
        var items = await _itemsRepository.GetAllAsync(cancellationToken);
        
        var groupedByUser = usageHistory
            .Join(items,
                usage => usage.ItemId,
                item => item.Id,
                (usage, item) => new { Usage = usage, UserId = item.CreatedById })
            .GroupBy(u => u.UserId);
    
        var combinationsStatistics = new List<CombinationStatisticsDto>();
    
        foreach (var userGroup in groupedByUser)
        {
            var userId = userGroup.Key;
            var userUsageHistory = userGroup.OrderBy(u => u.Usage.CreatedDateUtc).ToList();
            
            for (int i = 0; i < userUsageHistory.Count; i++)
            {
                var currentUsage = userUsageHistory[i];
                var currentItemName = await _itemsRepository.GetOneAsync(currentUsage.Usage.ItemId, cancellationToken);
                var combination = new List<string> { currentItemName.Name };
                
                for (int j = i + 1; j < userUsageHistory.Count; j++)
                {
                    var nextUsage = userUsageHistory[j];
                    
                    if ((nextUsage.Usage.CreatedDateUtc - currentUsage.Usage.CreatedDateUtc).TotalMinutes <= 5)
                    {
                        var itemName = await _itemsRepository.GetOneAsync(nextUsage.Usage.ItemId, cancellationToken);
                        combination.Add(itemName.Name);
                    }
                    else
                    {
                        break;
                    }
                }
                
                if (combination.Count > 1)
                {
                    combinationsStatistics.Add(new CombinationStatisticsDto
                    {
                        UserId = userGroup.Key.ToString(),
                        Combination = combination,
                        UsageCount = combination.Count
                    });
                }
            }
        }
    
        return combinationsStatistics;
    }

    private string GetSeason(DateTime date)
    {
        switch (date.Month)
        {
            case 12:
            case 1:
            case 2:
                return "Winter";
            case 3:
            case 4:
            case 5:
                return "Spring";
            case 6:
            case 7:
            case 8:
                return "Summer";
            case 9:
            case 10:
            case 11:
                return "Autumn";
            default:
                return "Unknown";
        }
    }
}