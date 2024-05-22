using Application.GlobalInstance;
using Application.IRepositories;
using Application.IServices;
using Application.Models.CreateDtos;
using Application.Models.Dtos;
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