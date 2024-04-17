using Application.IRepositories;
using Domain.Entities;
using Domain.Enums;
using Persistence.Database;
using Persistence.Repositories.Base;
using MongoDB.Driver;

namespace Persistence.Repositories;

public class ItemsRepository(MongoDbContext db) : BaseRepository<Item>(db, "Items"), IItemsRepository 
{
    public async Task<Item> UpdateAsync(Item item, CancellationToken cancellationToken)
    {
        var updateDefinition = MongoDB.Driver.Builders<Item>.Update
            .Set(c=> c.Name, item.Name)
            .Set(c=>c.Description, item.Description)
            .Set(c => c.Category, (Categories)item.Category)
            .Set(c => c.BrandId, item.BrandId)
            .Set(c=>c.CollectionId, item.CollectionId)
            .Set(c => c.LastModifiedDateUtc, item.LastModifiedDateUtc)
            .Set(c => c.LastModifiedById, item.LastModifiedById);

        var options = new MongoDB.Driver.FindOneAndUpdateOptions<Item>
        {
            ReturnDocument = ReturnDocument.After
        };

        return await this._collection.FindOneAndUpdateAsync(MongoDB.Driver.Builders<Item>.Filter.Eq(u => u.Id, item.Id), 
            updateDefinition, 
            options, 
            cancellationToken);
    }
}