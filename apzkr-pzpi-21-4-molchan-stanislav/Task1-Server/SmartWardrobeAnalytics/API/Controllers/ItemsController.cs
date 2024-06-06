using Application.IServices;
using Application.Models.CreateDtos;
using Application.Models.Dtos;
using Application.Models.UpdateDtos;
using Application.Paging;
using Microsoft.AspNetCore.Mvc;

namespace API.Controllers;

[ApiController]
[Route("api/[controller]")]
public class ItemsController : ControllerBase
{
    private readonly IItemsService _itemsService;

    public ItemsController(IItemsService itemsService)
    {
        _itemsService = itemsService;
    }

    [HttpGet("{id}")]
    public async Task<ActionResult<ItemDto>> GetItemByIdAsync(string id, CancellationToken cancellationToken)
    {
        var item = await _itemsService.GetItemByIdAsync(id, cancellationToken);
        if (item == null)
        {
            return NotFound();
        }
        return Ok(item);
    }
    
    [HttpGet("collection/{id}")]
    public async Task<ActionResult<ItemDto>> GetItemByCollectionIdAsync(string id, CancellationToken cancellationToken)
    {
        var item = await _itemsService.GetItemByCollectionIdAsync(id, cancellationToken);
        if (item == null)
        {
            return NotFound();
        }
        return Ok(item);
    }

    [HttpPost]
    public async Task<ActionResult<ItemDto>> CreateItemAsync([FromBody] ItemCreateDto dto, CancellationToken cancellationToken)
    {
        var newItem = await _itemsService.CreateItemAsync(dto, cancellationToken);
        return Ok(newItem);
    }

    [HttpPut]
    public async Task<ActionResult<ItemDto>> UpdateItemAsync([FromBody] ItemUpdateDto dto, CancellationToken cancellationToken)
    {
        var updatedItem = await _itemsService.UpdateItemAsync(dto, cancellationToken);
        if (updatedItem == null)
        {
            return NotFound();
        }
        return Ok(updatedItem);
    }

    [HttpDelete("{id}")]
    public async Task<ActionResult> DeleteItemAsync(string id, CancellationToken cancellationToken)
    {
        var result = await _itemsService.DeleteItemAsync(id, cancellationToken);
        if (!result)
        {
            return NotFound();
        }
        return NoContent();
    }

    [HttpGet]
    public async Task<ActionResult<PagedList<ItemDto>>> GetItemsWithPaginationAsync([FromQuery] int pageNumber, [FromQuery] int pageSize, CancellationToken cancellationToken)
    {
        var items = await _itemsService.GetItemsWithPaginationAsync(pageNumber, pageSize, cancellationToken);
        return Ok(items);
    }
}