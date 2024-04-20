using Application.IServices;
using Application.Models.CreateDtos;
using Application.Models.Dtos;
using Application.Models.UpdateDtos;
using Application.Paging;
using Microsoft.AspNetCore.Mvc;

namespace API.Controllers;

[ApiController]
[Route("api/[controller]")]
public class BrandBonusesController : ControllerBase
{
    private readonly IBrandBonusService _brandBonusService;

    public BrandBonusesController(IBrandBonusService brandBonusService)
    {
        _brandBonusService = brandBonusService;
    }

    [HttpGet("{id}")]
    public async Task<ActionResult<BrandBonusDto>> GetByIdAsync(string id, CancellationToken cancellationToken)
    {
        var brandBonus = await _brandBonusService.GetByIdAsync(id, cancellationToken);
        if (brandBonus == null)
        {
            return NotFound();
        }
        return Ok(brandBonus);
    }

    [HttpPost]
    public async Task<ActionResult<BrandBonusDto>> CreateAsync([FromBody] BrandBonusCreateDto dto, CancellationToken cancellationToken)
    {
        var newBrandBonus = await _brandBonusService.CreateAsync(dto, cancellationToken);
        return Ok(newBrandBonus);
    }

    [HttpPut("{id}")]
    public async Task<ActionResult<BrandBonusDto>> UpdateAsync(string id, [FromBody] BrandBonusUpdateDto dto, CancellationToken cancellationToken)
    {
        var updatedBrandBonus = await _brandBonusService.UpdateAsync(id, dto, cancellationToken);
        if (updatedBrandBonus == null)
        {
            return NotFound();
        }
        return Ok(updatedBrandBonus);
    }

    [HttpDelete("{id}")]
    public async Task<ActionResult> DeleteAsync(string id, CancellationToken cancellationToken)
    {
        var result = await _brandBonusService.DeleteAsync(id, cancellationToken);
        if (!result)
        {
            return NotFound();
        }
        return NoContent();
    }

    [HttpGet]
    public async Task<ActionResult<PagedList<BrandBonusDto>>> GetWithPaginationAsync([FromQuery] int pageNumber, [FromQuery] int pageSize, CancellationToken cancellationToken)
    {
        var brandBonuses = await _brandBonusService.GetWithPaginationAsync(pageNumber, pageSize, cancellationToken);
        return Ok(brandBonuses);
    }
}