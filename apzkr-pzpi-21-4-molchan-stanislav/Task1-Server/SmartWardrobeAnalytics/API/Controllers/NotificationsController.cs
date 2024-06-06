using Application.IServices;
using Application.Models.CreateDtos;
using Application.Models.Dtos;
using Application.Models.UpdateDtos;
using Microsoft.AspNetCore.Mvc;

namespace API.Controllers;

[ApiController]
[Route("api/notifications")]
public class NotificationsController : ControllerBase
{
    private readonly INotificationService _notificationService;

    public NotificationsController(INotificationService notificationService)
    {
        _notificationService = notificationService;
    }
    
    [HttpPost]
    public async Task<IActionResult> Create([FromBody] NotificationCreateDto notificationCreateDto, CancellationToken cancellationToken)
    {
        var createdNotification = await _notificationService.Create(notificationCreateDto, cancellationToken);
        return Ok(createdNotification);
    }
    
    [HttpGet("get")]
    public async Task<ActionResult<List<NotificationDto>>> GetNotifications(CancellationToken cancellationToken)
    {
        var result = await _notificationService.GetForUserAll(cancellationToken);

        return result;
    }
    
    [HttpGet("get/{id}")]
    public async Task<ActionResult<List<NotificationDto>>> GetNotifications(string id, CancellationToken cancellationToken)
    {
        var result = await _notificationService.GetForItem(id, cancellationToken);

        return Ok(result);
    }
    
    [HttpGet("get/by-id/{id}")]
    public async Task<ActionResult<NotificationDto>> GetNotification(string id, CancellationToken cancellationToken)
    {
        var result = await _notificationService.GetById(id, cancellationToken);

        return Ok(result);
    }
    
    [HttpPut]
    public async Task<ActionResult<ItemDto>> UpdateItemAsync([FromBody] NotificationUpdateDto dto, CancellationToken cancellationToken)
    {
        var updatedItem = await _notificationService.UpdateAsync(dto, cancellationToken);
        if (updatedItem == null)
        {
            return NotFound();
        }
        return Ok(updatedItem);
    }

    [HttpDelete("{id}")]
    public async Task<ActionResult> DeleteItemAsync(string id, CancellationToken cancellationToken)
    {
        var result = await _notificationService.DeleteItemAsync(id, cancellationToken);
        if (!result)
        {
            return NotFound();
        }
        return NoContent();
    }
}