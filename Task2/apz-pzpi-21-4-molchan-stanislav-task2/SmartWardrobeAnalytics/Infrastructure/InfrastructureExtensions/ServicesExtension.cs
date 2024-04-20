using System.Collections;
using System.Text;
using Application.IRepositories;
using Application.IServices;
using Application.IServices.Identity;
using Application.IServices.Statistics;
using Infrastructure.Services;
using Infrastructure.Services.Identity;
using Microsoft.AspNetCore.Authentication.JwtBearer;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.IdentityModel.Tokens;

namespace Infrastructure.InfrastructureExtensions;

public static class ServicesExtension
{
    public static IServiceCollection AddServices(this IServiceCollection services, IConfiguration configuration)
    {
        services.AddScoped<IUserService, UsersService>();
        services.AddScoped<ITokenService, TokensService>();
        services.AddScoped<IPasswordHasher, PasswordHasher>();
        services.AddScoped<ICollectionsService, CollectionsService>();
        services.AddScoped<IItemsService, ItemsService>();
        services.AddScoped<IBrandsService, BrandsService>();
        services.AddScoped<IBrandBonusService, BaseBrandBonusService>();
        services.AddScoped<IOfferService, OffersService>();
        services.AddScoped<IUsageService, UsagesService>();
        services.AddScoped<IRFIDTagsService, RFIDTagsService>();
        services.AddScoped<IStatisticsService, StatisticsService>();

        return services;
    }
    
    public static IServiceCollection AddJWTTokenAuthentication(this IServiceCollection services, IConfiguration configuration)
    {
        services
            .AddAuthentication(JwtBearerDefaults.AuthenticationScheme)
            .AddJwtBearer(options =>
            {
                options.TokenValidationParameters = new TokenValidationParameters
                {
                    ValidateIssuer = configuration.GetValue<bool>("JsonWebTokenKeys:ValidateIssuer"),
                    ValidateAudience = configuration.GetValue<bool>("JsonWebTokenKeys:ValidateAudience"),
                    ValidateLifetime = configuration.GetValue<bool>("JsonWebTokenKeys:ValidateLifetime"),
                    ValidateIssuerSigningKey = configuration.GetValue<bool>("JsonWebTokenKeys:ValidateIssuerSigningKey"),
                    ValidIssuer = configuration.GetValue<string>("JsonWebTokenKeys:ValidIssuer"),
                    ValidAudience = configuration.GetValue<string>("JsonWebTokenKeys:ValidAudience"),
                    IssuerSigningKey = new SymmetricSecurityKey(Encoding.UTF8.GetBytes(configuration.GetValue<string>("JsonWebTokenKeys:IssuerSigningKey"))),
                    ClockSkew = TimeSpan.Zero
                };
            });

        return services;
    }
}