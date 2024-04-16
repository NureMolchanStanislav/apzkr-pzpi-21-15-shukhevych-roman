using Application.IRepositories;
using Domain.Entities;
using Persistence.Database;
using Persistence.Repositories.Base;

namespace Persistence.Repositories;

public class NotificationsRepository(MongoDbContext db)
    : BaseRepository<Notification>(db, "Notifications"), INotificationsRepository;