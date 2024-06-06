using Application.IRepositories;
using Domain.Entities;
using Persistence.Database;
using Persistence.Repositories.Base;
using MongoDB.Driver;

namespace Persistence.Repositories;

public class CollectionsRepository(MongoDbContext db)
    : BaseRepository<Collection>(db, "Collections"), ICollectionsRepository
{
    public async Task<Collection> UpdateAsync(Collection collection, CancellationToken cancellationToken)
    {
        var updateDefinition = MongoDB.Driver.Builders<Collection>.Update
            .Set(c=> c.Name, collection.Name)
            .Set(c=>c.Description, collection.Description)
            .Set(c => c.LastModifiedDateUtc, collection.LastModifiedDateUtc)
            .Set(c => c.LastModifiedById, collection.LastModifiedById);

        var options = new MongoDB.Driver.FindOneAndUpdateOptions<Collection>
        {
            ReturnDocument = ReturnDocument.After
        };

        return await this._collection.FindOneAndUpdateAsync(MongoDB.Driver.Builders<Collection>.Filter.Eq(u => u.Id, collection.Id), 
            updateDefinition, 
            options, 
            cancellationToken);
    }
}