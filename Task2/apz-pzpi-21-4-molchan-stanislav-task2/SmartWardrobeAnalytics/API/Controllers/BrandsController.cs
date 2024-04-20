using Application.IServices;
using Application.Models.CreateDtos;
using Application.Models.Dtos;
using Application.Models.UpdateDtos;
using Application.Paging;
using Microsoft.AspNetCore.Mvc;

namespace API.Controllers;

[ApiController]
[Route("api/[controller]")]
public class BrandsController : ControllerBase
{
    private readonly IBrandsService _brandsService;

    public BrandsController(IBrandsService brandsService)
    {
        _brandsService = brandsService;
    }

    [HttpGet("{id}")]
    public async Task<ActionResult<BrandDto>> GetByIdAsync(string id, CancellationToken cancellationToken)
    {
        var brand = await _brandsService.GetByIdAsync(id, cancellationToken);
        if (brand == null)
        {
            return NotFound();
        }
        return Ok(brand);
    }

    [HttpPost]
    public async Task<ActionResult<BrandDto>> CreateAsync([FromBody] BrandCreateDto dto, CancellationToken cancellationToken)
    {
        var newBrand = await _brandsService.CreateAsync(dto, cancellationToken);
        return Ok(newBrand);
    }

    [HttpPut]
    public async Task<ActionResult<BrandDto>> UpdateAsync([FromBody] BrandUpdateDto dto, CancellationToken cancellationToken)
    {
        var updatedBrand = await _brandsService.UpdateAsync(dto, cancellationToken);
        if (updatedBrand == null)
        {
            return NotFound();
        }
        return Ok(updatedBrand);
    }

    [HttpDelete("{id}")]
    public async Task<ActionResult> DeleteAsync(string id, CancellationToken cancellationToken)
    {
        var result = await _brandsService.DeleteAsync(id, cancellationToken);
        if (!result)
        {
            return NotFound();
        }
        return NoContent();
    }

    [HttpGet]
    public async Task<ActionResult<PagedList<BrandDto>>> GetWithPaginationAsync([FromQuery] int pageNumber, [FromQuery] int pageSize, CancellationToken cancellationToken)
    {
        var brands = await _brandsService.GetWithPaginationAsync(pageNumber, pageSize, cancellationToken);
        return Ok(brands);
    }
}