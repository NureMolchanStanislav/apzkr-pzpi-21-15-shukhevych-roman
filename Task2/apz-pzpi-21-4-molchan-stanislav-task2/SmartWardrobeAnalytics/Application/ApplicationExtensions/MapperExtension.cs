using System.Reflection;
using Application.MappingProfile;
using Domain.Entities;
using Microsoft.Extensions.DependencyInjection;

namespace Application.ApplicationExtensions;

public static class MapperExtension
{
    public static IServiceCollection AddMapper(this IServiceCollection services)
    {
        services.AddAutoMapper(Assembly.GetAssembly(typeof(UserProfile)));
        services.AddAutoMapper(Assembly.GetAssembly(typeof(RoleProfile)));
        services.AddAutoMapper(Assembly.GetAssembly(typeof(BrandProfile)));
        services.AddAutoMapper(Assembly.GetAssembly(typeof(CollectionProfile)));
        services.AddAutoMapper(Assembly.GetAssembly(typeof(BrandBonusProfile)));
        services.AddAutoMapper(Assembly.GetAssembly(typeof(ItemProfile)));
        services.AddAutoMapper(Assembly.GetAssembly(typeof(OfferProfile)));
        services.AddAutoMapper(Assembly.GetAssembly(typeof(Usages)));

        return services;
    }
}