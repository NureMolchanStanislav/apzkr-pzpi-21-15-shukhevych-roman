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
    
    [HttpGet("popular-items")]
    public async Task<ActionResult<List<PopularItemStatisticsDto>>> GetTopPopularItemsAsync(string brandId, DateTime startDate, DateTime endDate, int topCount, CancellationToken cancellationToken)
    {
        var topPopularItems = await _statisticsService.GetTopPopularItemsAsync(brandId, startDate, endDate, topCount, cancellationToken);
        return Ok(topPopularItems);
    }
}