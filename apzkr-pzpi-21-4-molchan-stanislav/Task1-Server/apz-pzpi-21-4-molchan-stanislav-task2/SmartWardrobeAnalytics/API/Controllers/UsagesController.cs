using System.Security.Claims;
using Application.IServices;
using Application.Models.CreateDtos;
using Application.Models.UpdateDtos;
using Microsoft.AspNetCore.Mvc;

namespace API.Controllers;

[ApiController]
[Route("api/[controller]")]
public class UsagesController : ControllerBase
{
    private readonly IUsageService _usageService;

    public UsagesController(IUsageService usageService)
    {
        _usageService = usageService;
    }

    [HttpGet("{id}")]
    public async Task<IActionResult> GetById(string id, CancellationToken cancellationToken)
    {
        var usage = await _usageService.GetByIdAsync(id, cancellationToken);
        if (usage == null)
        {
            return NotFound($"Usage with ID {id} not found.");
        }

        return Ok(usage);
    }

    [HttpPost]
    public async Task<IActionResult> Create([FromBody] UsagesCreateDto usagesCreateDto, CancellationToken cancellationToken)
    {
        var createdUsage = await _usageService.CreateAsync(usagesCreateDto, cancellationToken);
        return CreatedAtAction(nameof(GetById), new { id = createdUsage.Id }, createdUsage);
    }

    [HttpPut("{id}")]
    public async Task<IActionResult> Update(string id, [FromBody] UsagesUpdateDto usagesUpdateDto, CancellationToken cancellationToken)
    {
        var updatedUsage = await _usageService.UpdateAsync(usagesUpdateDto, cancellationToken);
        if (updatedUsage == null)
        {
            return NotFound($"Usage with ID {id} not found.");
        }

        return Ok(updatedUsage);
    }

    [HttpDelete("{id}")]
    public async Task<IActionResult> Delete(string id, CancellationToken cancellationToken)
    {
        bool result = await _usageService.DeleteAsync(id, cancellationToken);
        if (!result)
        {
            return NotFound($"Usage with ID {id} not found.");
        }

        return NoContent();
    }

    [HttpGet]
    public async Task<IActionResult> GetWithPagination([FromQuery] int pageNumber, [FromQuery] int pageSize, CancellationToken cancellationToken)
    {
        var pagedResults = await _usageService.GetWithPaginationAsync(pageNumber, pageSize, cancellationToken);
        return Ok(pagedResults);
    }
    
    [HttpGet("totalUsageByBrand/{brandId}")]
    public async Task<IActionResult> GetTotalBrandUsageByUser(string brandId, CancellationToken cancellationToken)
    {
        int totalUsages = await _usageService.CalculateTotalBrandUsageByUser(brandId, cancellationToken);

        if (totalUsages == 0)
        {
            return NotFound($"No usages found for brand ID {brandId} by user ID");
        }

        return Ok(totalUsages);
    }

    [HttpGet("totalUsageByItem/{itemId}")]
    public async Task<ActionResult<int>> GetTotalUsageByItem(string itemId, CancellationToken cancellationToken)
    {
        var result = await _usageService.GetTotalUsageByItem(itemId, cancellationToken);
        return Ok(result);
    }
}