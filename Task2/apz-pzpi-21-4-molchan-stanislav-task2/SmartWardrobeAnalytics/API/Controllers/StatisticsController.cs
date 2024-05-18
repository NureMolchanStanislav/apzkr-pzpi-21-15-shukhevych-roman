using Application.IServices.Statistics;
using Application.Models.StatisticsDtos;
using Infrastructure.Services;
using Microsoft.AspNetCore.Mvc;
using MongoDB.Bson;

namespace API.Controllers;

[ApiController]
[Route("api/[controller]")]
public class StatisticsController : ControllerBase
{
    private readonly IStatisticsService _statisticsService;

    public StatisticsController(IStatisticsService statisticsService)
    {
        _statisticsService = statisticsService;
    }

    [HttpGet("seasonal-item-usage")]
    public async Task<ActionResult<List<SeasonalItemUsageStatisticsDto>>> GetSeasonalItemUsageStatisticsAsync([FromQuery] string brandId, CancellationToken cancellationToken)
    {
        if (!ObjectId.TryParse(brandId, out ObjectId parsedBrandId))
        {
            return BadRequest("Invalid brandId format.");
        }

        var statistics = await _statisticsService.GetSeasonalItemUsageStatisticsAsync(parsedBrandId, cancellationToken);
        return Ok(statistics);
    }
    
    [HttpGet("combo")]
    public async Task<ActionResult<List<CombinationStatisticsDto>>> GetItemCombinationsStatisticsAsync(CancellationToken cancellationToken)
    {
        var statistics = await _statisticsService.GetItemCombinationsStatisticsAsync(cancellationToken);
        return Ok(statistics);
    }

    [HttpGet("item-statistic/{itemId}")]
    public async Task<ActionResult<List<MonthlyItemUsageStatisticsDto>>> GetMonthlyItemUsageStatisticsAsync(
        string itemId, int months, CancellationToken cancellationToken)
    {
        var statistics = await _statisticsService.GetMonthlyItemUsageStatisticsAsync(itemId, months, cancellationToken);
        return Ok(statistics);
    }
    
    [HttpGet("item-usages/{itemId}")]
    public async Task<ActionResult<List<ItemUsagesHistory>>> GetUsageHistoryForItemAsync(string itemId, CancellationToken cancellationToken)
    {
        var result = await _statisticsService.GetUsageHistoryForItemAsync(itemId, cancellationToken);
        return Ok(result);
    }
    
    [HttpGet("popular-items")]
    public async Task<ActionResult<List<PopularItemStatisticsDto>>> GetTopPopularItemsAsync(string brandId, DateTime startDate, DateTime endDate, int topCount, CancellationToken cancellationToken)
    {
        var topPopularItems = await _statisticsService.GetTopPopularItemsAsync(brandId, startDate, endDate, topCount, cancellationToken);
        return Ok(topPopularItems);
    }
}