using Application.IRepositories;
using Domain.Entities;
using MongoDB.Bson;
using Persistence.Database;
using Persistence.Repositories.Base;
using MongoDB.Driver;

namespace Persistence.Repositories;

public class UsagesRepository(MongoDbContext db) : BaseRepository<Usages>(db, "Usages"), IUsagesRepository
{
    public async Task<Usages> UpdateAsync(Usages usages, CancellationToken cancellationToken)
    {
        var updateDefinition = MongoDB.Driver.Builders<Usages>.Update
            .Set(c=> c.TotalCount, usages.TotalCount)
            .Set(c => c.LastEvent, usages.LastEvent)
            .Set(c => c.LastModifiedDateUtc, usages.LastModifiedDateUtc)
            .Set(c => c.LastModifiedById, usages.LastModifiedById);

        var options = new MongoDB.Driver.FindOneAndUpdateOptions<Usages>
        {
            ReturnDocument = ReturnDocument.After
        };

        return await this._collection.FindOneAndUpdateAsync(MongoDB.Driver.Builders<Usages>.Filter.Eq(u => u.Id, usages.Id), 
            updateDefinition, 
            options, 
            cancellationToken);
    }
    
    public async Task<Usages> IncrementTotalCountAsync(string id, CancellationToken cancellationToken)
    {
        var updateDefinition = MongoDB.Driver.Builders<Usages>.Update
            .Inc(c => c.TotalCount, 1);

        var options = new MongoDB.Driver.FindOneAndUpdateOptions<Usages>
        {
            ReturnDocument = MongoDB.Driver.ReturnDocument.After
        };

        return await this._collection.FindOneAndUpdateAsync(MongoDB.Driver.Builders<Usages>.Filter.Eq(u => u.ItemId, ObjectId.Parse(id)),
            updateDefinition,
            options,
            cancellationToken);
    }
    
    public async Task<int> GetTotalUsageCountForItems(List<ObjectId> itemIds, CancellationToken cancellationToken)
    {
        var filter = Builders<Usages>.Filter.In(u => u.ItemId, itemIds) & Builders<Usages>.Filter.Eq(u => u.IsDeleted, false);
        var totalUsages = await _collection.Aggregate()
            .Match(filter)
            .Group(new BsonDocument { { "_id", BsonNull.Value }, { "total", new BsonDocument("$sum", "$TotalCount") } })
            .FirstOrDefaultAsync(cancellationToken);

        return totalUsages == null ? 0 : totalUsages["total"].AsInt32;
    }
}