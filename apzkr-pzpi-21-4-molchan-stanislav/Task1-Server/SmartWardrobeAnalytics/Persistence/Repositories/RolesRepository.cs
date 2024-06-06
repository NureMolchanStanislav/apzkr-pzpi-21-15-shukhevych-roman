using System.Linq.Expressions;
using Application.IRepositories;
using Domain.Entities;
using MongoDB.Bson;
using MongoDB.Driver;
using Persistence.Database;
using Persistence.Repositories.Base;

namespace Persistence.Repositories;

public class RolesRepository(MongoDbContext db) : BaseRepository<Role>(db, "Roles"), IRolesRepository
{
    public async Task<Role> GetRoleAsync(ObjectId id, CancellationToken cancellationToken)
    {
        return await (await this._collection.FindAsync(x => x.Id == id && x.IsDeleted == false)).FirstOrDefaultAsync(cancellationToken);
    }

    public async Task<Role> GetRoleAsync(Expression<Func<Role, bool>> predicate, CancellationToken cancellationToken)
    {
        return await (await this._collection.FindAsync(Builders<Role>.Filter.Where(predicate) & Builders<Role>.Filter.Where(x => !x.IsDeleted))).FirstOrDefaultAsync(cancellationToken);
    }
}