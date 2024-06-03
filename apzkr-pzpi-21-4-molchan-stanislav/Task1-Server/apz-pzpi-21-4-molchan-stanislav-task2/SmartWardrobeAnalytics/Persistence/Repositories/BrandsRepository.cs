using Application.IRepositories;
using Domain.Entities;
using Persistence.Database;
using Persistence.Repositories.Base;
using MongoDB.Driver;

namespace Persistence.Repositories;

public class BrandsRepository(MongoDbContext db) : BaseRepository<Brand>(db, "Brands"), IBrandsRepository
{
    public async Task<Brand> UpdateAsync(Brand brand, CancellationToken cancellationToken)
    {
        var updateDefinition = MongoDB.Driver.Builders<Brand>.Update
            .Set(c=> c.Name, brand.Name)
            .Set(c => c.LastModifiedDateUtc, brand.LastModifiedDateUtc)
            .Set(c => c.LastModifiedById, brand.LastModifiedById);

        var options = new MongoDB.Driver.FindOneAndUpdateOptions<Brand>
        {
            ReturnDocument = ReturnDocument.After
        };

        return await this._collection.FindOneAndUpdateAsync(MongoDB.Driver.Builders<Brand>.Filter.Eq(u => u.Id, brand.Id), 
            updateDefinition, 
            options, 
            cancellationToken);
    }
}