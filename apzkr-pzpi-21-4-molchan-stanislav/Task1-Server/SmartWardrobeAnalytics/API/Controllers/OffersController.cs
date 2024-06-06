using Application.IServices;
using Application.Models.CreateDtos;
using Application.Models.Dtos;
using Application.Models.UpdateDtos;
using Microsoft.AspNetCore.Mvc;

namespace API.Controllers;

[ApiController]
[Route("api/[controller]")]
public class OffersController : ControllerBase
{
    private readonly IOfferService _offerService;

    public OffersController(IOfferService offerService)
    {
        _offerService = offerService;
    }

    [HttpGet("{id}")]
    public async Task<IActionResult> GetById(string id, CancellationToken cancellationToken)
    {
        var offer = await _offerService.GetByIdAsync(id, cancellationToken);
        if (offer == null)
        {
            return NotFound();
        }

        return Ok(offer);
    }

    [HttpPost]
    public async Task<IActionResult> Create([FromBody] OfferCreateDto offerCreateDto, CancellationToken cancellationToken)
    {
        var createdOffer = await _offerService.CreateAsync(offerCreateDto, cancellationToken);
        return CreatedAtAction(nameof(GetById), new { id = createdOffer.Id }, createdOffer);
    }

    [HttpPut("{id}")]
    public async Task<IActionResult> Update(string id, [FromBody] OfferUpdateDto offerUpdateDto, CancellationToken cancellationToken)
    {
        var updatedOffer = await _offerService.UpdateAsync(id, offerUpdateDto, cancellationToken);
        if (updatedOffer == null)
        {
            return NotFound();
        }

        return Ok(updatedOffer);
    }

    [HttpDelete("{id}")]
    public async Task<IActionResult> Delete(string id, CancellationToken cancellationToken)
    {
        bool result = await _offerService.DeleteAsync(id, cancellationToken);
        if (!result)
        {
            return NotFound();
        }

        return NoContent();
    }

    [HttpGet]
    public async Task<IActionResult> GetWithPagination([FromQuery] int pageNumber, [FromQuery] int pageSize, CancellationToken cancellationToken)
    {
        var pagedOffers = await _offerService.GetWithPaginationAsync(pageNumber, pageSize, cancellationToken);
        return Ok(pagedOffers);
    }

    [HttpGet("user-offer")]
    public async Task<ActionResult<List<OfferInfo>>> GetForUser(CancellationToken cancellationToken)
    {
        var result = await _offerService.GetForUser(cancellationToken);
        return Ok(result);
    }
}