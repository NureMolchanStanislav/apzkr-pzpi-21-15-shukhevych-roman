using Application.Models.CreateDtos;
using Application.Models.Dtos;

namespace Application.IServices;

public interface INotificationService
{
    public Task<NotificationDto> Create(NotificationCreateDto createDto, CancellationToken cancellationToken);
    
    public Task<List<NotificationDto>> GetForUserAll(CancellationToken cancellationToken);
}