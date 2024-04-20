using Application.Models.CreateDtos;
using Application.Models.Dtos;
using Application.Models.UpdateDtos;
using AutoMapper;
using Domain.Entities;

namespace Application.MappingProfile;

public class BrandBonusProfile : Profile
{
    public BrandBonusProfile()
    {
        CreateMap<BrandBonus, BrandBonusDto>();
        CreateMap<BrandBonusDto, BrandBonus>();
        CreateMap<BrandBonusCreateDto, BrandBonus>();
        CreateMap<BrandBonus, BrandBonusCreateDto>();
        CreateMap<BrandBonusUpdateDto, BrandBonus>();
        CreateMap<BrandBonus, BrandBonusUpdateDto>();
    }
}