using Application.GlobalInstance;
using Application.IRepositories;
using Application.IServices;
using Application.Models.CreateDtos;
using Application.Models.Dtos;
using Application.Models.UpdateDtos;
using AutoMapper;
using Domain.Entities;
using MongoDB.Bson;

namespace Infrastructure.Services;

public class NotificationService : INotificationService
{
    private readonly INotificationsRepository _notificationsRepository;
    private readonly IMapper _mapper;

    public NotificationService(INotificationsRepository notificationsRepository, IMapper mapper)
    {
        _notificationsRepository = notificationsRepository;
        _mapper = mapper;
    }

    public async Task<List<NotificationDto>> GetForUserAll(CancellationToken cancellationToken)
    {
        var notifications = await 
            _notificationsRepository.GetAllAsync(x => x.CreatedById == GlobalUser.Id, cancellationToken);
        var results = _mapper.Map<List<NotificationDto>>(notifications);

        return results;
    }
    
    public async Task<List<NotificationDto>> GetForItem(string itemId, CancellationToken cancellationToken)
    {
        var notifications = await 
            _notificationsRepository.GetAllAsync(x => x.ItemId == ObjectId.Parse(itemId), cancellationToken);
        var results = _mapper.Map<List<NotificationDto>>(notifications);

        return results;
    }
    
    public async Task<NotificationDto> GetById(string notificationId, CancellationToken cancellationToken)
    {
        var notifications = await 
            _notificationsRepository.GetOneAsync(x => x.Id == ObjectId.Parse(notificationId), cancellationToken);
        var result = _mapper.Map<NotificationDto>(notifications);

        return result;
    }
    
    public async Task<Notification> UpdateAsync(NotificationUpdateDto dto, CancellationToken cancellationToken)
    {
        var existingOffer = await _notificationsRepository.GetOneAsync(ObjectId.Parse(dto.Id), cancellationToken);
        if (existingOffer == null)
        {
            return null;
        }

        _mapper.Map(dto, existingOffer);
        await _notificationsRepository.UpdateAsync(existingOffer, cancellationToken);

        return _mapper.Map<Notification>(existingOffer);
    }
    
    public async Task<bool> DeleteItemAsync(string id, CancellationToken cancellationToken)
    {
        var existingItem = await _notificationsRepository.GetOneAsync(ObjectId.Parse(id), cancellationToken);

        if (existingItem == null)
        {
            return false;
        }

        await _notificationsRepository.DeleteAsync(existingItem, cancellationToken);
        return true;
    }
    
    public async Task<NotificationDto> Create(NotificationCreateDto createDto, CancellationToken cancellationToken)
    {
        var notification = await
            _notificationsRepository.AddAsync(new Notification()
            {
                Condition = createDto.Condition,
                Description = createDto.Description,
                Title = createDto.Title,
                ItemId = ObjectId.Parse(createDto.ItemId),
                CreatedDateUtc = DateTime.UtcNow,
                CreatedById = GlobalUser.Id

            }, cancellationToken);

        var result = _mapper.Map<NotificationDto>(notification);

        return result;
    }
}