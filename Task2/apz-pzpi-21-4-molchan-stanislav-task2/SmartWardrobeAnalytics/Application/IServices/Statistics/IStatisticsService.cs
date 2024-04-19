using Application.Models.StatisticsDtos;
using MongoDB.Bson;

namespace Application.IServices.Statistics;

public interface IStatisticsService
{
    Task<List<SeasonalItemUsageStatisticsDto>> GetSeasonalItemUsageStatisticsAsync(ObjectId brandId,
        CancellationToken cancellationToken);
    
    Task<List<CombinationStatisticsDto>> GetItemCombinationsStatisticsAsync(CancellationToken cancellationToken);
}