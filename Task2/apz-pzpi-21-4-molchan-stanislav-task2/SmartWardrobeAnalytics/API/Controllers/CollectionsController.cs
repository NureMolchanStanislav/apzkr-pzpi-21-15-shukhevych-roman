using Application.IServices;
using Application.Models.CreateDtos;
using Application.Models.Dtos;
using Application.Models.UpdateDtos;
using Application.Paging;
using Domain.Entities;
using Microsoft.AspNetCore.Mvc;

namespace API.Controllers;

[ApiController]
[Route("api/[controller]")]
public class CollectionsController : ControllerBase
{
    private readonly ICollectionsService _collectionsService;

    public CollectionsController(ICollectionsService collectionsService)
    {
        _collectionsService = collectionsService;
    }

    [HttpPost]
    public async Task<ActionResult<Collection>> CreateCollectionAsync([FromBody] CollectionCreateDto collection, CancellationToken cancellationToken)
    {
        var result = await _collectionsService.CreateCollectionAsync(collection, cancellationToken);
        return Ok(result);
    }

    [HttpGet("{id}")]
    public async Task<ActionResult<CollectionDto>> GetCollectionByIdAsync(string id, CancellationToken cancellationToken)
    {
        var collection = await _collectionsService.GetCollectionByIdAsync(id, cancellationToken);
        if (collection == null)
        {
            return NotFound();
        }
        return Ok(collection);
    }

    [HttpPut]
    public async Task<ActionResult<Collection>> UpdateCollectionAsync([FromBody] CollectionUpdateDto collection, CancellationToken cancellationToken)
    {
        var updatedCollection = await _collectionsService.UpdateCollectionAsync(collection, cancellationToken);
        if (updatedCollection == null)
        {
            return NotFound();
        }
        return Ok(updatedCollection);
    }

    [HttpDelete("{id}")]
    public async Task<ActionResult<bool>> DeleteCollectionAsync(string id, CancellationToken cancellationToken)
    {
        var result = await _collectionsService.DeleteCollectionAsync(id, cancellationToken);
        if (!result)
        {
            return NotFound();
        }
        return Ok(result);
    }

    [HttpGet]
    public async Task<ActionResult<PagedList<CollectionDto>>> GetCollectionsWithPaginationAsync([FromQuery] int pageNumber, [FromQuery] int pageSize, CancellationToken cancellationToken)
    {
        var collections = await _collectionsService.GetCollectionsWithPaginationAsync(pageNumber, pageSize, cancellationToken);
        return Ok(collections);
    }
}