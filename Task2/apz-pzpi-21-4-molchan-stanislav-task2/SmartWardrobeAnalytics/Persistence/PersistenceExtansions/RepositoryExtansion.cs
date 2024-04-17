using System.Collections;
using Application.IRepositories;
using Microsoft.Extensions.DependencyInjection;
using Persistence.Database;
using Persistence.Repositories;

namespace Persistence.PersistenceExtensions;

public static class RepositoryExtension
{
    public static IServiceCollection AddInfrastructure(this IServiceCollection services)
    {
        services.AddSingleton<MongoDbContext>();

        services.AddScoped<IUsersRepository, UsersRepository>();
        services.AddScoped<IRolesRepository, RolesRepository>();
        services.AddScoped<IRefreshTokensRepository, RefreshTokensRepository>();
        services.AddScoped<ICollectionsRepository, CollectionsRepository>();
        services.AddScoped<IItemsRepository, ItemsRepository>();

        return services;
    }
}