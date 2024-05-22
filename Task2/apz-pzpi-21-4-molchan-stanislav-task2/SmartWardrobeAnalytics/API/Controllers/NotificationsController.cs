using Application.IServices;
using Application.Models.CreateDtos;
using Application.Models.Dtos;
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
}