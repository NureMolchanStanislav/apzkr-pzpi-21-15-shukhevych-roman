using Domain.Entities;
using Microsoft.Extensions.DependencyInjection;
using MongoDB.Bson;
using MongoDB.Driver;
using Persistence.Database;

namespace Persistence.PersistenceExtensions;

public class DbInitializer
{
    private readonly IMongoCollection<User> _userCollection;

    private readonly IMongoCollection<Role> _roleCollection;

    public DbInitializer(IServiceProvider serviceProvider)
    {
        _userCollection = serviceProvider.GetService<MongoDbContext>().Db.GetCollection<User>("Users");
        _roleCollection = serviceProvider.GetService<MongoDbContext>().Db.GetCollection<Role>("Roles");
    }

    public async Task InitialaizeDb(CancellationToken cancellationToken)
    {
        await AddRoles(cancellationToken);
    }

    public async Task AddRoles(CancellationToken cancellationToken)
    {
        var roles = new Role[]
        {
            new Role
            {
                Name = "User"
            },

            new Role
            {
                Name = "Admin"
            },
            
            new Role
            {
                Name = "Owner"
            }
        };

        await _roleCollection.InsertManyAsync(roles);
    }

    public async Task AddUsers(CancellationToken cancellationToken)
    {

        var users = new User[]
        {
            new User
            {
                Id = ObjectId.Parse("6533bb29c8c22b038c71cf46"),
                CreatedById = ObjectId.Parse("6533bb29c8c22b038c71cf46"),
                CreatedDateUtc = DateTime.UtcNow,
                LastModifiedById = ObjectId.Parse("6533bb29c8c22b038c71cf46"),
                LastModifiedDateUtc = DateTime.UtcNow,
                IsDeleted = false
            },

            new User
            {
                Id = ObjectId.Parse("6533bde5755745116be42ce7"),
                Phone = "+380953326869",
                Email = "mykhailo.bilodid@nure.ua",
                PasswordHash = "Yuiop12345",
                CreatedById = ObjectId.Parse("6533bde5755745116be42ce7"),
                CreatedDateUtc = DateTime.UtcNow,
                LastModifiedById = ObjectId.Parse("6533bde5755745116be42ce7"),
                LastModifiedDateUtc = DateTime.UtcNow,
                IsDeleted = false
            },

            new User
            {
                Id = ObjectId.Parse("6533bded80fbc6e96250575b"),
                Phone = "+380953826869",
                Email = "bondarenko.taras@gmail.com",
                PasswordHash = "taeas12345",
                CreatedById = ObjectId.Parse("6533bded80fbc6e96250575b"),
                CreatedDateUtc = DateTime.UtcNow,
                LastModifiedById = ObjectId.Parse("6533bded80fbc6e96250575b"),
                LastModifiedDateUtc = DateTime.UtcNow,
                IsDeleted = false            
                }
        };

        await _userCollection.InsertManyAsync(users);
    }
}