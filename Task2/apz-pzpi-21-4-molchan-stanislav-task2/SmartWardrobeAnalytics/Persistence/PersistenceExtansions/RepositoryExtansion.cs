using System.Collections;
using Application.IRepositories;
using Application.IServices;
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
        services.AddScoped<IBrandsRepository, BrandsRepository>();
        services.AddScoped<IBrandBonusesRepository, BrandBonusesRepository>();
        services.AddScoped<IOffersRepository, OffersRepository>();
        services.AddScoped<IUsagesRepository, UsagesRepository>();
        services.AddScoped<IRFIDTagsRepository, RFIDTagsRepository>();
        services.AddScoped<IUsageHistoryRepository, UsageHistoryRepository>();

        return services;
    }
}