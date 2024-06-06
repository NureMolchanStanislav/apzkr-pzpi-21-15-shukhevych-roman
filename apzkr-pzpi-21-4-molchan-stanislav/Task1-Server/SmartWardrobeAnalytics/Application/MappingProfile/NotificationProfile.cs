using Application.Models.Dtos;
using Application.Models.UpdateDtos;
using AutoMapper;
using Domain.Entities;

namespace Application.MappingProfile;

public class NotificationProfile : Profile
{
    public NotificationProfile()
    {
        CreateMap<NotificationDto, Notification>();
        CreateMap<Notification, NotificationDto>();
        CreateMap<NotificationUpdateDto, Notification>();
        CreateMap<Notification, NotificationUpdateDto>();
    }
}