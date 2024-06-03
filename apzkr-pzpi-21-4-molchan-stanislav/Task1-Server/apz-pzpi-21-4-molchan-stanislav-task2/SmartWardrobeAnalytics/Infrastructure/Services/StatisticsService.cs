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
            var combinationDate = currentUsage.Usage.CreatedDateUtc;  // Запам'ятовуємо дату першого елемента комбінації
            
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
                    UsageCount = combination.Count,
                    Date = combinationDate.ToString()
                });
            }
        }
    }

    return combinationsStatistics;
}
    
    public async Task<List<PopularItemStatisticsDto>> GetTopPopularItemsAsync(string brandId, DateTime startDate, DateTime endDate, int topCount, CancellationToken cancellationToken)
    {
        var usageHistory = await _usageHistoryRepository.GetPageAsync(1, 500, 
            u => u.CreatedDateUtc >= startDate && u.CreatedDateUtc <= endDate, cancellationToken);
        
        var items = await _itemsRepository.GetPageAsync(1, topCount, 
            x => x.BrandId == ObjectId.Parse(brandId), cancellationToken);
        
        var usageForBrandItems = usageHistory.Where(u => items.Any(i => i.Id == u.ItemId));
        
        var groupedByItem = usageForBrandItems
            .GroupBy(u => u.ItemId)
            .Select(g => new { ItemId = g.Key, UsageCount = g.Count() })
            .OrderByDescending(x => x.UsageCount)
            .Take(topCount);
        
        var popularItems = new List<PopularItemStatisticsDto>();
        foreach (var group in groupedByItem)
        {
            var itemName = await GetItemNameByIdAsync(group.ItemId, cancellationToken);
            popularItems.Add(new PopularItemStatisticsDto { ItemName = itemName, UsageCount = group.UsageCount });
        }

        return popularItems;
    }
    
    public async Task<List<MonthlyItemUsageStatisticsDto>> GetMonthlyItemUsageStatisticsAsync(
        string itemId, int months, CancellationToken cancellationToken)
    {
        var endDate = DateTime.UtcNow;
        var startDate = endDate.AddMonths(-months);

        var usageHistory = await _usageHistoryRepository.GetPageAsync(
            1, 1000, x => x.ItemId == ObjectId.Parse(itemId) && x.CreatedDateUtc >= startDate && x.CreatedDateUtc <= endDate, cancellationToken);

        var monthlyUsages = new Dictionary<string, int>();
        
        foreach (var usage in usageHistory)
        {
            var monthKey = usage.CreatedDateUtc.ToString("yyyy-MM");
            if (!monthlyUsages.ContainsKey(monthKey))
            {
                monthlyUsages[monthKey] = 0;
            }
            monthlyUsages[monthKey]++;
        }
        
        var monthlyStatisticsList = monthlyUsages.Select(kvp => new MonthlyItemUsageStatisticsDto
        {
            Month = kvp.Key,
            UsageCount = kvp.Value
        }).ToList();

        return monthlyStatisticsList;
    }
    
    private async Task<string> GetItemNameByIdAsync(ObjectId itemId, CancellationToken cancellationToken)
    {
        var item = await _itemsRepository.GetOneAsync(x => x.Id == itemId, cancellationToken);
        return item?.Name;
    }

    public async Task<List<ItemUsagesHistory>> GetUsageHistoryForItemAsync(string itemId, CancellationToken cancellationToken)
    {
        var item = await _itemsRepository.GetOneAsync(x => x.Id == ObjectId.Parse(itemId), cancellationToken);
        var usages = await _usageHistoryRepository.GetAllAsync(x => x.ItemId == ObjectId.Parse(itemId), cancellationToken);
        var dtos = usages.Select(usage => new ItemUsagesHistory()
        {
            Name = item.Name,
            Date = usage.CreatedDateUtc.ToString()
        }).ToList();
        
        return dtos;
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