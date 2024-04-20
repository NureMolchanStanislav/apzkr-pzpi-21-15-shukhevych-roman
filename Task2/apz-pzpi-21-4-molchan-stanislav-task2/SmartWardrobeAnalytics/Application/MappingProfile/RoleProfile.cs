using Application.Models.Dtos;
using AutoMapper;
using Domain.Entities;

namespace Application.MappingProfile;

public class RoleProfile : Profile
{
    public RoleProfile()
    {
        CreateMap<Role, RoleDto>().ReverseMap();
        CreateMap<RoleDto, Role>();
    }
}

