using Application.Models.CreateDtos;
using Application.Models.Dtos;
using Application.Models.UpdateDtos;
using AutoMapper;
using Domain.Entities;

namespace Application.MappingProfile;

public class BrandProfile : Profile
{
    public BrandProfile()
    {
        CreateMap<Brand, BrandDto>();
        CreateMap<BrandDto, Brand>();
        CreateMap<BrandCreateDto, Brand>();
        CreateMap<Brand, BrandCreateDto>();
        CreateMap<BrandUpdateDto, Brand>();
        CreateMap<Brand, BrandUpdateDto>();
    }
}