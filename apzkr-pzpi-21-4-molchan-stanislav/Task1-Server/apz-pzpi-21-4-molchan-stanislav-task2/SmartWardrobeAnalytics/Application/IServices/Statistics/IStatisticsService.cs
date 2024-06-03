using Application.Models.StatisticsDtos;
using MongoDB.Bson;

namespace Application.IServices.Statistics;

public interface IStatisticsService
{
    Task<List<SeasonalItemUsageStatisticsDto>> GetSeasonalItemUsageStatisticsAsync(ObjectId brandId,
        CancellationToken cancellationToken);
    
    Task<List<CombinationStatisticsDto>> GetItemCombinationsStatisticsAsync(CancellationToken cancellationToken);

    Task<List<PopularItemStatisticsDto>> GetTopPopularItemsAsync(string brandId, DateTime startDate, DateTime endDate,
        int topCount, CancellationToken cancellationToken);

    Task<List<MonthlyItemUsageStatisticsDto>> GetMonthlyItemUsageStatisticsAsync(
        string itemId, int months, CancellationToken cancellationToken);

    Task<List<ItemUsagesHistory>> GetUsageHistoryForItemAsync(string itemId, CancellationToken cancellationToken);
}