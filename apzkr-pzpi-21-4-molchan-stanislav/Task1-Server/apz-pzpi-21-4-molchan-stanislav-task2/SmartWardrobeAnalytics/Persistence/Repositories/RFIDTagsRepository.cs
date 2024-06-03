using Application.IRepositories;
using Application.Models.UpdateDtos;
using Domain.Entities;
using MongoDB.Bson;
using Persistence.Database;
using Persistence.Repositories.Base;
using MongoDB.Driver;

namespace Persistence.Repositories;

public class RFIDTagsRepository(MongoDbContext db) : BaseRepository<RFIDTag>(db, "RFIDTags"), IRFIDTagsRepository
{
    public async Task<bool> GetStatus(string sensorId, CancellationToken cancellationToken)
    {
        var filter = MongoDB.Driver.Builders<RFIDTag>.Filter.Eq(x => x.TagId, sensorId);
        var sensor = await _collection.Find(filter).FirstOrDefaultAsync();

        if (sensor.Status)
        {
            return true;
        }
        
        return false;
    }
    
    public async Task<bool> UpdateStatus(string tagId, CancellationToken cancellationToken)
    {
        bool currentStatus = await GetStatus(tagId, cancellationToken);
        
        var filter = MongoDB.Driver.Builders<RFIDTag>.Filter.Eq(x => x.TagId, tagId);
        var update = MongoDB.Driver.Builders<RFIDTag>.Update
            .Set(x => x.Status, !currentStatus)
            .Set(x => x.LastModifiedDateUtc, DateTime.UtcNow);

        var result = await _collection.UpdateOneAsync(filter, update);
        
        return !currentStatus;
    }
    
    public async Task<bool> UpdateItemId(string tagId, string itemId, CancellationToken cancellationToken)
    {
        var filter = MongoDB.Driver.Builders<RFIDTag>.Filter.Eq(x => x.Id, ObjectId.Parse(tagId));
        var update = MongoDB.Driver.Builders<RFIDTag>.Update
            .Set(x => x.ItemId, ObjectId.Parse(itemId));

        var result = await _collection.UpdateOneAsync(filter, update);

        return true;
    }
}