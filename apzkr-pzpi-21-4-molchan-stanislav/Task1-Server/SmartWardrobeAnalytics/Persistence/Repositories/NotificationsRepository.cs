using Application.IRepositories;
using Domain.Entities;
using Persistence.Database;
using Persistence.Repositories.Base;
using MongoDB.Driver;

namespace Persistence.Repositories;

public class NotificationsRepository(MongoDbContext db)
    : BaseRepository<Notification>(db, "Notifications"), INotificationsRepository
{
    
    public async Task<Notification> UpdateAsync(Notification item, CancellationToken cancellationToken)
    {
        var updateDefinition = MongoDB.Driver.Builders<Notification>.Update
            .Set(c=> c.Title, item.Title)
            .Set(c=>c.Description, item.Description)
            .Set(c=>c.Condition, item.Condition)
            .Set(c=>c.ItemId, item.ItemId)
            .Set(c => c.LastModifiedDateUtc, item.LastModifiedDateUtc)
            .Set(c => c.LastModifiedById, item.LastModifiedById);

        var options = new MongoDB.Driver.FindOneAndUpdateOptions<Notification>
        {
            ReturnDocument = ReturnDocument.After
        };

        return await this._collection.FindOneAndUpdateAsync(MongoDB.Driver.Builders<Notification>.Filter.Eq(u => u.Id, item.Id), 
            updateDefinition, 
            options, 
            cancellationToken);
    }
}