using Application.Models.CreateDtos;
using Application.Models.Dtos;
using Application.Models.UpdateDtos;
using AutoMapper;
using Domain.Entities;

namespace Application.MappingProfile;

public class UsagesProfile : Profile
{
    public UsagesProfile()
    {
        CreateMap<Usages, UsageDto>();
        CreateMap<UsageDto, Usages>();
        CreateMap<UsagesCreateDto, Usages>();
        CreateMap<Usages, UsagesCreateDto>();
        CreateMap<UsagesUpdateDto, Usages>();
        CreateMap<Usages, UsagesUpdateDto>();
    }
}