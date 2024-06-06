using Application.ApplicationExtensions;
using Infrastructure.InfrastructureExtensions;
using Persistence.PersistenceExtensions;

var builder = WebApplication.CreateBuilder(args);

// Add services to the container.
builder.Services.AddMapper();
builder.Services.AddInfrastructure();
builder.Services.AddServices(builder.Configuration);
builder.Services.AddJWTTokenAuthentication(builder.Configuration);
builder.Services.AddControllers();
builder.Services.AddEndpointsApiExplorer();
builder.Services.AddSwaggerGen();
builder.Services.AddAuthorization(options =>
{
    options.AddPolicy("AdminPolicy", policy => policy.RequireRole("Admin"));
    options.AddPolicy("UserPolicy", policy => policy.RequireRole("User"));
    options.AddPolicy("OwnerPolicy", policy => policy.RequireRole("Owner"));
});
builder.Services.AddCors(opt =>
{
    opt.AddPolicy("CorsPolicy", policy =>
    {
        policy.AllowAnyHeader().AllowAnyMethod().AllowAnyOrigin();
    });
});

var app = builder.Build();

// Configure the HTTP request pipeline.
if (app.Environment.IsDevelopment())
{
    app.UseSwagger();
    app.UseSwaggerUI();
}

app.UseHttpsRedirection();

app.UseCors("CorsPolicy");

app.UseAuthentication();
app.UseAuthorization();

app.MapControllers();


/*using var scope = app.Services.CreateScope();
var serviceProvider = scope.ServiceProvider;
var initializer = new DbInitializer(serviceProvider);
await initializer.InitialaizeDb(CancellationToken.None);*/


app.Run();

public partial class Program { }
