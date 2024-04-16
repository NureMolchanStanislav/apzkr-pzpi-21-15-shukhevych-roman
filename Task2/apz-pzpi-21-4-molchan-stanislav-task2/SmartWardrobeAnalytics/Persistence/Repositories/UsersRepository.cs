using Application.IRepositories;
using Domain.Entities;
using MongoDB.Driver;
using Persistence.Database;
using Persistence.Repositories.Base;

namespace Persistence.Repositories;

public class UsersRepository(MongoDbContext db) : BaseRepository<User>(db, "Users"), IUsersRepository
{
    public async Task<User> UpdateUserAsync(User user, CancellationToken cancellationToken)
    {
        var updateDefinition = Builders<User>.Update
            .Set(u => u.FirstName, user.FirstName)
            .Set(u => u.LastName, user.LastName)
            .Set(u => u.Email, user.Email)
            .Set(u => u.Phone, user.Phone)
            .Set(u => u.PasswordHash, user.PasswordHash)
            .Set(u => u.Roles, user.Roles)
            .Set(u => u.City, user.City)
            .Set(u=>u.Country, user.Country)
            .Set(u => u.LastModifiedDateUtc, user.LastModifiedDateUtc)
            .Set(u => u.LastModifiedById, user.LastModifiedById);

        var options = new FindOneAndUpdateOptions<User>
        {
            ReturnDocument = ReturnDocument.After
        };

        return await this._collection.FindOneAndUpdateAsync(
            Builders<User>.Filter.Eq(u => u.Id, user.Id), 
            updateDefinition, 
            options, 
            cancellationToken);
    }
}