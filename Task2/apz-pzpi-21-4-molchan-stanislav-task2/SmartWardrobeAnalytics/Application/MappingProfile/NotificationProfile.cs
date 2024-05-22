using Application.Models.Dtos;
using AutoMapper;
using Domain.Entities;

namespace Application.MappingProfile;

public class NotificationProfile : Profile
{
    public NotificationProfile()
    {
        CreateMap<NotificationDto, Notification>();
        CreateMap<Notification, NotificationDto>();
    }
}