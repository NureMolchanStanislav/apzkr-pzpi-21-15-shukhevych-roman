using Application.Models.CreateDtos;
using Application.Models.Dtos;
using Application.Models.UpdateDtos;
using Domain.Entities;

namespace Application.IServices;

public interface INotificationService
{
    public Task<NotificationDto> Create(NotificationCreateDto createDto, CancellationToken cancellationToken);
    
    public Task<List<NotificationDto>> GetForUserAll(CancellationToken cancellationToken);

    Task<NotificationDto> GetById(string notificationId, CancellationToken cancellationToken);

    Task<List<NotificationDto>> GetForItem(string itemId, CancellationToken cancellationToken);

    Task<Notification> UpdateAsync(NotificationUpdateDto dto, CancellationToken cancellationToken);

    Task<bool> DeleteItemAsync(string id, CancellationToken cancellationToken);
}