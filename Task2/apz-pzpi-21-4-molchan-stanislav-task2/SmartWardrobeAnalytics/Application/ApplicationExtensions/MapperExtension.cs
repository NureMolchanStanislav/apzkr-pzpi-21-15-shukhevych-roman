using System.Reflection;
using Application.MappingProfile;
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

        return services;
    }
}